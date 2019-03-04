package com.vtube.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="comments")
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "title", nullable = false, columnDefinition = "LONGTEXT")
	private String title;
	
	@Column(name = "likes", nullable = false)
	private int likes = 0;
	
	@Column(name = "dislikes", nullable = false)
	private int dislikes = 0;
	
	//Every comment have an author
	@ManyToOne
	@JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"))
	private User author;
	
	//Every comment have video on which it is written
	@ManyToOne
	@JoinTable(name = "videos", joinColumns = @JoinColumn(name = "video_id"))
	private Video commentedVideo;
	
	//Comment may have parent comment on which it is written
	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name= "super_comment_id"))
	private Comment superComment;
}
