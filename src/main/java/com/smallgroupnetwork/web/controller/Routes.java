package com.smallgroupnetwork.web.controller;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 5:15 PM
 */
public class Routes
{
	public static final String API = "/api";
	public static final String ADMIN_API = API + "/admin";

	private static final String ACCOUNT_PATH = "/account";
	public static final String ADMIN_ACCOUNT = ADMIN_API + ACCOUNT_PATH;

	public static final String STUDY_PATH = "/study";
	public static final String STUDY = API + STUDY_PATH;
	public static final String ADMIN_STUDY = ADMIN_API + STUDY_PATH;

	private static final String ATTACH_PATH = "/attach";
	public static final String ADMIN_ATTACH = ADMIN_API + ATTACH_PATH;
	public static final String ATTACH = API + ATTACH_PATH;

	private static final String CAROUSEL_SLIDE_PATH = "/slide";
	public static final String ADMIN_CAROUSEL_SLIDE = ADMIN_API + CAROUSEL_SLIDE_PATH;
	public static final String CAROUSEL_SLIDE = API + CAROUSEL_SLIDE_PATH;

	public static final String TRENDING_PATH = "/trending";
	public static final String TRENDING = API + TRENDING_PATH;

}
