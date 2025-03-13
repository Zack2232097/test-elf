package com.sky.logtool;

public class Keywords {
    public static boolean MgtReCnt(String line) {
        return line.contains("adapt_session_data_changes: device_index = 1, device_addr =");
    }

    public static boolean ChallengeReq(String line) {
        return line.contains("dkmgt_handle_challenge_req_ack_from_dkauthmgr");
    }

    public static boolean ChallengeRsp(String line) {
        return line.contains("dkmgt_handle_uploadpdudata_notify_from_bleinfomgr: index=0 authsts=CHANLLENGE_REQ_SEND_REMOTE");
    }

    public static boolean AuthPass(String line) {
        return line.contains("dkmgt_forward_to_bleinfomgr: msg_type=1, msg_id=3, index=0, payload_len=2");
    }

    public static String UserPreInfo(String line) {
        if (line.contains("UserPreInfo"))
            return "UserPreInfo";
        return null;
    }
}