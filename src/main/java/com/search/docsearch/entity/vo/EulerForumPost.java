package com.search.docsearch.entity.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EulerForumPost implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("username")
    private String username;
    @JsonProperty("avatar_template")
    private String avatarTemplate;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("cooked")
    private String cooked;
    @JsonProperty("post_number")
    private String postNumber;
    @JsonProperty("post_type")
    private String postType;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("reply_count")
    private String replyCount;
    @JsonProperty("reply_to_post_number")
    private String replyToPostNumber;
    @JsonProperty("quote_count")
    private String quoteCount;
    @JsonProperty("incoming_link_count")
    private String incomingLinkCount;
    @JsonProperty("reads")
    private String reads;
    @JsonProperty("score")
    private String score;
    @JsonProperty("topic_id")
    private String topicId;
    @JsonProperty("topic_slug")
    private String topicSlug;
    @JsonProperty("topic_title")
    private String topicTitle;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("display_username")
    private String displayUsername;
    @JsonProperty("primary_group_name")
    private String primaryGroupName;
    @JsonProperty("flair_name")
    private String flairName;
    @JsonProperty("version")
    private String version;
    @JsonProperty("user_title")
    private String userTitle;
    @JsonProperty("bookmarked")
    private String bookmarked;
    @JsonProperty("raw")
    private String raw;
    @JsonProperty("moderator")
    private String moderator;
    @JsonProperty("admin")
    private String admin;
    @JsonProperty("staff")
    private String staff;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("hidden")
    private String hidden;
    @JsonProperty("trust_level")
    private String trustLevel;
    @JsonProperty("deleted_at")
    private String deletedAt;
    @JsonProperty("user_deleted")
    private String userDeleted;
    @JsonProperty("edit_reason")
    private String editReason;
    @JsonProperty("wiki")
    private String wiki;
    @JsonProperty("reviewable_id")
    private String reviewableId;
    @JsonProperty("reviewable_score_count")
    private String reviewableScoreCount;
    @JsonProperty("reviewable_score_pending_count")
    private String reviewableScorePendingCount;
    @JsonProperty("topic_posts_count")
    private String topicPostsCount;
    @JsonProperty("topic_filtered_posts_count")
    private String topicFilteredPostsCount;
    @JsonProperty("topic_archetype")
    private String topicArchetype;
    @JsonProperty("category_slug")
    private String categorySlug;
}
