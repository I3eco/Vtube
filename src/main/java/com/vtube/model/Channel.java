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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "channels")
@Entity
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	// One user can have multiple channels
	@ManyToOne
	@JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"))
	private User owner;

	// One channel can have multiple videos
	@OneToMany
	private Set<Video> ownedVideos;

	// Many users can subscribe to many channels
	@ManyToMany
	@JoinTable(name = "subscribed_channels", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> usersSubscribedToChannel;

}
