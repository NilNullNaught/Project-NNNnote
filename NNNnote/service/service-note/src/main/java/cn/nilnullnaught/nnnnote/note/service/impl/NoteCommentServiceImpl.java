package cn.nilnullnaught.nnnnote.note.service.impl;

import cn.nilnullnaught.nnnnote.client.user.UserDynamicClient;
import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import cn.nilnullnaught.nnnnote.note.mapper.NoteCommentMapper;
import cn.nilnullnaught.nnnnote.note.mapper.NoteInfoMapper;
import cn.nilnullnaught.nnnnote.note.service.NoteCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 笔记评论表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Service
public class NoteCommentServiceImpl extends ServiceImpl<NoteCommentMapper, NoteComment> implements NoteCommentService {

    @Autowired
    private NoteInfoMapper noteInfoMapper;

    @Autowired
    private UserDynamicClient userDynamicClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 发布评论
     *
     * @param noteComment
     */
    @Override
    @Transactional
    public void postComment(NoteComment noteComment) {
        var noteId = noteComment.getNoteId();

        // region <- 更新笔记的评论数 ->

        var uw = new UpdateWrapper<NoteInfo>();
        uw.eq("id", noteId);
        uw.setSql("comment_count = comment_count+1");
        noteInfoMapper.update(uw.getEntity(), uw);

        // endregion

        // region <- 如果属于回复评论，则被回复评论的回复数 +1 ->

        var replyCommentId = noteComment.getReplyCommentId();
        if (replyCommentId != null) {
            var uw2 = new UpdateWrapper<NoteComment>();
            uw2.eq("id", replyCommentId);
            uw2.setSql("reply_count = reply_count+1");
            baseMapper.update(uw2.getEntity(), uw2);
        }

        // endregion



        // region <- 新增评论 ->

        noteComment.setId(IdWorker.get32UUID());// 由于添加动态需要评论Id，所以在这里进行设置
        baseMapper.insert(noteComment);

        // endregion



        // region <- 添加到动态 ->

        var userDynamic = new UserDynamic();
        userDynamic.setUserId(noteComment.getUserId());
        userDynamic.setDynamicType(2);
        userDynamic.setDynamicId(noteComment.getId());
        userDynamic.setDescription("发表评论");

        userDynamicClient.createDynamic(userDynamic);

        // endregion

    }

    /**
     * 获取评论数据
     *
     * @param noteId
     * @param current
     * @param limit
     * @param sortCondition
     * @return
     */
    @Override
    public Map<String, Object> getComments(String noteId, long current, long limit, String sortCondition) {
        // region <- 获取评论 ->

        var qw = new QueryWrapper<NoteComment>();
        qw.eq("note_id", noteId);
        qw.isNull("reply_comment_id");
        switch (sortCondition) {
            case "likes":
                qw.orderByDesc("likes");
                break;
            case "timeAsc":
                qw.orderByAsc("gmt_create");
                break;
            case "timeDesc":
                qw.orderByDesc("gmt_create");
                break;
            default:
                qw.orderByAsc(sortCondition);
        }



        var page = new Page<NoteComment>(current, limit);
        baseMapper.selectPage(page, qw);
        var items = page.getRecords();
        // endregion

        // region <- 获取每条评论下的回复（超出三条则只返回三条） ->
        var replies = new HashMap<String, List<NoteComment>>();
        for (var noteComment : items) {
            var id = noteComment.getId();

            var qw2 = new QueryWrapper<NoteComment>();
            qw2.eq("reply_comment_id", id);
            qw2.orderByAsc("gmt_create");
            qw2.last("limit 3");
            var rawResult = baseMapper.selectList(qw2);
            replies.put(id, rawResult);
        }
        // endregion

        //把分页数据提取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", items);
        map.put("replies", replies);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());

        return map;
    }


    /**
     * 获取评论下的所有回复
     *
     * @param replyCommentId
     * @return
     */
    @Override
    public List<NoteComment> getReplies(String replyCommentId) {
        var qw = new QueryWrapper<NoteComment>();
        qw.eq("reply_comment_id", replyCommentId);
        qw.orderByAsc("gmt_create");

        return baseMapper.selectList(qw);
    }

    /**
     * 评论点赞与取消
     *
     * @param userId
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> likeComment(String userId, String commentId) {
        // region <- 判断 redis 中是否存在 commentId 对应的键，如果不存在则创建 ->
        var keyExist = redisTemplate.hasKey(commentId);

        if (keyExist) {
            Boolean exist = redisTemplate.boundSetOps(commentId).isMember(userId);
        } else {
            var setKey = redisTemplate.boundSetOps("setKey");
            redisTemplate.expire(commentId, 1, TimeUnit.DAYS);
        }
        // endregion

        // region <- 判断用户是否已经对该笔记点过赞。并执行相应操作 ->
        Boolean exist = redisTemplate.boundSetOps(commentId).isMember(userId);

        var uw = new UpdateWrapper<NoteComment>();
        uw.eq("id", commentId);
        if (exist) {
            // 用户已经点过赞，取消赞
            uw.setSql("likes = likes-1");
            baseMapper.update(uw.getEntity(), uw);
            redisTemplate.boundSetOps(commentId).remove(userId);
        } else {
            // 用户没有点过赞，赞数加一
            uw.setSql("likes = likes+1");
            baseMapper.update(uw.getEntity(), uw);
            redisTemplate.boundSetOps(commentId).add(userId);
        }
        // endregion

        // region <- 获取评论当前的点赞数 ->
        var qw = new QueryWrapper<NoteComment>();
        qw.eq("id", commentId);
        qw.select("likes");
        var likes = baseMapper.selectOne(qw).getLikes();
        // endregion

        var map = new HashMap<String,Object>();
        map.put("likes",likes);
        map.put("exist",exist);
        return map;
    }

}
