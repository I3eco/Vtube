package com.vtube.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "nick_name", nullable = false, unique = true)
	private String nickName;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false, columnDefinition = "LONGTEXT")
	private String password;

	@Column(name = "age", nullable = false)
	private int age;
	
	//One user can have multiple channels
	@OneToMany(mappedBy = "owner")
	private Set<Channel> ownedChannels;
	
	//One user can have many comments
	@OneToMany(mappedBy = "author")
	private Set<Comment> comments;
	
	//User have videos he likes
    @ManyToMany
    @JoinTable(
    		name= "liked_videos",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private Set<Video> likedVideos;
    
    //Many users can subscribe to many channels
    @ManyToMany
    @JoinTable(
    		name= "subscribed_channels",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "channel_id"))
    private Set<Channel> subscribedChannels;
    
    //History of watched videos for each user
    @ManyToMany
    @JoinTable(
    		name= "videos_history",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private Set<Video> watchedVideos;
    
    //Users can have videos for watching later
    @ManyToMany
    @JoinTable(
    		name= "watch_later",
    		joinColumns= @JoinColumn(name= "user_id"),
    		inverseJoinColumns= @JoinColumn(name= "video_id"))
    private Set<Video> videosForLater;
}
