package com.vtube.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="videos")
@Entity
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "title", nullable = false, columnDefinition = "TEXT")
	private String title;
	
	@Column(name = "thumbnail", nullable = false, columnDefinition = "TEXT")
	private String thumbnail;
	
	@Column(name = "url", nullable = false, columnDefinition = "TEXT")
	private String url;
	
	@Column(name="description", columnDefinition = "LONGTEXT")
	private String description;
	
	@Column(name = "date_of_creation", nullable = false, columnDefinition = "DATE")
	private LocalDate dateOfCreation;
	
	@Column(name = "dislikes", nullable = false)
	private int dislikes = 0;
	
	//one video can have many comments
	@OneToMany(mappedBy = "commentedVideo")
	private Set<Comment> comments;
	
	//one channel can have multiple videos
	@ManyToOne
	@JoinColumn(name="channel_id", nullable = false)
	private Channel owner;
	
	//one video can be liked by many users
	//User have videos he likes
    @ManyToMany
    @JoinTable(
    		name= "liked_videos",
    		joinColumns= @JoinColumn(name= "video_id"),
    		inverseJoinColumns= @JoinColumn(name= "user_id"))
    private Set<User> usersWhoLikeThisVideo;
    
    //History of watched videos for each user
    @ManyToMany
    @JoinTable(
    		name= "videos_history",
    		joinColumns= @JoinColumn(name= "video_id"),
    		inverseJoinColumns= @JoinColumn(name= "user_id"))
    private Set<User> usersWatchedThisVideo;
    
    //Users can have videos for watching later
    @ManyToMany
    @JoinTable(
    		name= "watch_later",
    		joinColumns= @JoinColumn(name= "video_id"),
    		inverseJoinColumns= @JoinColumn(name= "user_id"))
    private Set<User> usersWantToWatchThisVideoLater;
}
