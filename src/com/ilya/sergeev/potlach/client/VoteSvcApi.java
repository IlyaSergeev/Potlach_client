package com.ilya.sergeev.potlach.client;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface VoteSvcApi
{
	public static final String VOTE_PATH = "/votes";
	
	public static final String SINGLE_VOTE_PATH = VOTE_PATH + "/{id}"; 
	
	public static final String ID_PARAM = "id";
	public static final String GIFT_ID_PARAM = "gift";
	public static final String VOTE_PARAM = "vote";
	
	@GET(VOTE_PATH)
	public Vote getVoteOfGift(@Query(GIFT_ID_PARAM) long giftId);
	
	@GET(SINGLE_VOTE_PATH)
	public Vote getVote(@Path(ID_PARAM) long ig);
	
	@POST(VOTE_PATH)
	public Vote sendVote(@Query(GIFT_ID_PARAM)long giftId, @Query(VOTE_PARAM) int voteValue);
}
