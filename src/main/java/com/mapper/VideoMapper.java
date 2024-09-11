package com.mapper;

import com.bean.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VideoMapper {
    // 插入新视频
    void insertVideo(Video video);

    // 更新视频信息
    void updateVideo(Video video);

    // 根据ID查询视频
    Video getVideoById(Integer id);

    // 获取所有待审核的视频
    List<Video> getVideosByStatus(@Param("status") String status);

    // 获取所有视频（已审核、已拒绝）
    List<Video> getAllVideos();
    // 获取所有已审核的视频
    List<Video> getAllCheckedVideos();

    // 删除视频
    void deleteVideo(Integer id);

    List<Video> getAllUnReviewVideos();


    void reviewVideo(@Param("id") int id,
                     @Param("status") String status,
                     @Param("updated_at") LocalDateTime updated_at,
                     @Param("reviewed_at") LocalDateTime reviewed_at,
                     @Param("reviewer") String reviewer);

    List<Video> searchVideos(@Param("query") String query);
    List<Video> findVideosByUsername(@Param("uploader") String uploader);

}
