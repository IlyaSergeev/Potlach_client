package com.ilya.sergeev.potlach.client;

import java.util.Collection;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;


public interface GiftSvcApi
{
	public static final String GIFT_PATH = "/gifts";
	
	public static final String SINGLE_GIFT_PATH = GIFT_PATH + "/{id}";
	public static final String ALL_GIFT_PATH = GIFT_PATH + "/all";
	public static final String MY_GIFT_PATH = GIFT_PATH + "/my";
	public static final String SEARCH_GIFT_PATH = GIFT_PATH + "/search";
	public static final String CREATE_GIFT_PATH = GIFT_PATH + "/create";
	public static final String SAVE_GIFT_PATH = GIFT_PATH + "/save";
	public static final String GIFT_DATA_PATH = SINGLE_GIFT_PATH + "/data";
	
	public static final String ID_PARAM = "id";
	public static final String TAG_PARAM = "tag";
	public static final String DATA_PARAMETER = "data";
	public static final String USER_PARAM = "user";
	
	@GET(ALL_GIFT_PATH)
	public Collection<Gift> getAllGifts();
	
	@GET(MY_GIFT_PATH)
	public Collection<Gift> getMyGifts();
	
	@GET(GIFT_PATH)
	public Collection<Gift> getGifts(@Query(USER_PARAM) String userName);
	
	@GET(SINGLE_GIFT_PATH)
	public Gift getGift(@Path(ID_PARAM) long id);
	
	@GET(SEARCH_GIFT_PATH)
	public Collection<Gift> searchGift(@Query(TAG_PARAM) String keyWord);
	
	@POST(CREATE_GIFT_PATH)
	public Gift createGift(@Body Gift gift);
	
	@POST(SAVE_GIFT_PATH)
	public Gift saveGift(@Body Gift gift);
	
	@Multipart
	@POST(GIFT_DATA_PATH)
	public ImageStatus setImageData(@Path(ID_PARAM) long id, @Part(DATA_PARAMETER) TypedFile imageFile);
	
	@Streaming
	@GET(GIFT_DATA_PATH)
	public Response getData(@Path(ID_PARAM) long id);
}
