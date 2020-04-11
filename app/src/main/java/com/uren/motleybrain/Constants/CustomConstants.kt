package com.uren.motleybrain.Constants

object CustomConstants {

    val APP_NAME = "Colorful Brains"

    //Group Request Types
    val LOGIN_USER = "loginUser"
    val APP_GOOGLE_PLAY_DEFAULT_LINK = "https://play.google.com/store/apps/details?id="
    val APP_PACKAGE_NAME = "uren.com.colorfulbrains"
    val APP_FB_URL = "https://colorful-intelligence.firebaseio.com/"

    //Character constants
    val CHAR_AMPERSAND = "@"
    val CHAR_COLON = ":"
    val CHAR_HYPHEN = "-"
    val CHAR_E = "E"
    val CHAR_H = "H"

    val GROUP_OP_CHOOSE_TYPE = "CHOOSE"
    val GROUP_OP_VIEW_TYPE = "VIEW"

    val CAMERA_TEXT = "CAMERA"
    val GALLERY_TEXT = "GALLERY"
    val FROM_FILE_TEXT = "FROM_FILE"

    val LOGIN_METHOD_GOOGLE = "google"
    val LOGIN_METHOD_NORMAL = "normal"

    val fb_child_users = "Users"
    val fb_child_name = "name"
    val fb_child_userid = "userid"
    val fb_child_username = "username"
    val fb_child_profilePhotoUrl = "profilePhotoUrl"
    val fb_child_email = "email"
    val fb_child_phone = "Phone"
    val fb_child_networks = "Networks"
    val fb_child_groups = "Groups"
    val fb_child_friends = "Friends"
    val fb_child_countryCode = "countryCode"
    val fb_child_dialCode = "dialCode"
    val fb_child_phoneNumber = "phoneNumber"
    val fb_child_whocompletedid = "whoCompletedId"
    val fb_child_urgency = "urgency"
    val fb_child_admin = "admin"
    val fb_child_login_method = "loginMethod"

    val fb_child_problems = "Problems"
    val fb_child_fixed = "fixed"
    val fb_child_platform = "platform"
    val fb_child_problemdesc = "problemDesc"
    val fb_child_problemphotourl = "problemPhotoUrl"
    val fb_child_whoopened = "whoOpened"

    val fb_child_value_android = "android"
    val fb_child_value_ios = "ios"
    val fb_child_value_bug = "bug"

    val fb_child_status_sendedrequest = "sendedRequest"
    val fb_child_status_friend = "friend"
    val fb_child_status_waiting = "waiting"
    val fb_child_status_all = "all"

    val fb_child_adminid = "adminId"
    val fb_child_groupphotourl = "groupPhotoUrl"
    val fb_child_createdat = "createdAt"

    val fb_child_usertask = "UserTask"
    val fb_child_grouptask = "GroupTask"
    val fb_child_assignedtime = "assignedTime"
    val fb_child_completedtime = "completedTime"
    val fb_child_completed = "completed"
    val fb_child_closed = "closed"
    val fb_child_assignedfromid = "assignedFromId"
    val fb_child_taskdesc = "taskDesc"
    val fb_child_members = "Members"
    val fb_child_type = "type"
    val fb_child_assignedfrom = "AssignedFrom"

    val FCM_CODE_BODY = "body"
    val FCM_CODE_TITLE = "title"
    val FCM_CODE_PHOTO_URL = "photoUrl"
    val FCM_CODE_NOTIFICATION = "notification"
    val FCM_CODE_DATA = "data"
    val FCM_CODE_TO = "to"
    val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    val FCM_CODE_MESSAGE_ID = "MESSAGE_ID"
    val FCM_CODE_USERID = "userid"

    val FB_CHILD_DEVICE_TOKEN = "DeviceToken"
    val FB_CHILD_TOKEN = "Token"
    val FB_CHILD_SIGNIN = "SignIn"

    //Animation Tags
    val ANIMATE_LEFT_TO_RIGHT = "ANIMATE_LEFT_TO_RIGHT"
    val ANIMATE_RIGHT_TO_LEFT = "ANIMATE_RIGHT_TO_LEFT"
    val ANIMATE_DOWN_TO_UP = "ANIMATE_DOWN_TO_UP"
    val ANIMATE_UP_TO_DOWN = "ANIMATE_UP_TO_DOWN"

    val photo_upload_new = "newPhoto"
    val photo_upload_change = "changePhoto"

    val APP_INVITATION_LINK = "https://myduties.page.link/wMv3"

    val ALGOLIA_APP_ID = "0NXABIBM4D"
    val ALGOLIA_SEARCH_API_KEY = "c93cf7a717073098d2d25938b3ee8d27"
    val ALGOLIA_INDEX_NAME = "MyDuties"

    val URGENT = "urgent"
    val NOT_URGENT = "noturgent"
    val ALL_URGENT = "allurgent"


    //************* numeric constants**********************

    val GROUP_NAME_MAX_LENGTH = 25
    val BIO_MAX_LENGTH = 250

    val RESPONSE_OK = 1

    //Share video properties
    val MAX_VIDEO_DURATION = 15
    val MAX_VIDEO_SIZE_IN_MB = 25

    val MAX_IMAGE_SIZE_1MB = 1048576
    val MAX_IMAGE_SIZE_1ANDHALFMB = 1572864
    val MAX_IMAGE_SIZE_2ANDHALFMB = 2621440
    val MAX_IMAGE_SIZE_5MB = 5242880

    //Photo Chosen items
    val CODE_GALLERY_POSITION = 0
    val CODE_CAMERA_POSITION = 1
    val CODE_SCREENSHOT_POSITION = 1
    val CODE_PHOTO_REMOVE = 2
    val CODE_VIDEO_REMOVE = 2
    val CODE_PHOTO_EDIT = 3
    val CODE_PLAY_VIDEO = 3

    //Phone num verify duration
    val VERIFY_PHONE_NUM_DURATION = 60

    //Feed paignation initial values
    val DEFAULT_FEED_PAGE_COUNT = 1
    val DEFAULT_FEED_PERPAGE_COUNT = 15 // EN AZ 4 OLMALI.
    val DEFAULT_FEED_RADIUS = 5000 //metre cinsinden
    var FILTERED_FEED_RADIUS = 5000 //metre cinsinden

    //Get Follower initial values
    val DEFAULT_GET_FOLLOWER_PAGE_COUNT = 1
    val DEFAULT_GET_FOLLOWER_PERPAGE_COUNT = 50 // EN AZ 4 OLMALI.

    val SHARE_TRY_COUNT = 2

    //Share
    val REQUEST_CODE_ENABLE_LOCATION = 3003

    //User posts gridView pagination
    val DEFAULT_PROFILE_GRIDVIEW_PAGE_COUNT = 1
    val DEFAULT_PROFILE_GRIDVIEW_PERPAGE_COUNT = 30 // EN AZ 4 OLMALI.

    //USER POST VIEW TYPE
    val USER_POST_VIEW_TYPE_GRID = 0
    val USER_POST_VIEW_TYPE_LIST = 1

    val MESSAGE_LIMIT_COUNT = 25
    val REC_MAXITEM_LIMIT_COUNT = 50

    val KEYBOARD_CHECK_VALUE = 200

    val FCM_MAX_MESSAGE_LEN = 30
    val MAX_ALLOWED_NOTIFICATION_SIZE = 4

    //Activity request codes
    val REQUEST_CODE_START_MESSAGE_LIST_ACTIVITY = 1001

    //FEED Exceptions
    val VIEW_RETRY = 0
    val VIEW_NO_POST_FOUND = 1
    val VIEW_SERVER_ERROR = 4

    //Orientation constants
    val ORIENTATION_LEFT_RIGHT = 0
    val ORIENTATION_RIGHT_LEFT = 1
    val ORIENTATION_TOPLEFT_BOTTOMRIGHT = 2
    val ORIENTATION_BOTTOMRIGHT_TOPLEFT = 3
    val ORIENTATION_TOP_BOTTOM = 4
    val ORIENTATION_BOTTOM_TOP = 5
}
