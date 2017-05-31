package connect.ui.activity.chat.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connect.db.MemoryDataManager;
import connect.db.SharedPreferenceUtil;
import connect.db.green.DaoHelper.ContactHelper;
import connect.db.green.DaoHelper.MessageHelper;
import connect.db.green.bean.ContactEntity;
import connect.db.green.bean.GroupEntity;
import connect.db.green.bean.MessageEntity;
import connect.im.model.FailMsgsManager;
import connect.ui.activity.R;
import connect.ui.activity.chat.ChatActivity;
import connect.ui.activity.chat.bean.MsgDefinBean;
import connect.ui.activity.chat.bean.MsgDirect;
import connect.ui.activity.chat.bean.MsgSender;
import connect.ui.activity.chat.bean.RecExtBean;
import connect.ui.activity.chat.bean.StickerCategory;
import connect.ui.activity.chat.model.content.BaseChat;
import connect.ui.activity.chat.model.content.FriendChat;
import connect.ui.activity.chat.model.content.GroupChat;
import connect.ui.activity.chat.model.content.NormalChat;
import connect.ui.activity.chat.model.content.RobotChat;
import connect.ui.activity.contact.bean.MsgSendBean;
import connect.ui.activity.home.bean.MsgNoticeBean;
import connect.ui.base.BaseApplication;
import connect.utils.ActivityUtil;
import connect.utils.RegularUtil;
import connect.utils.data.ResourceUtil;


/**
 * Created by gtq on 2016/12/5.
 */
public class ChatMsgUtil {

    private static String Tag = "ChatMsgUtil";

    /**
     * sender, get the message sender direction
     *
     * @param definBean
     * @return
     */
    public static MsgDirect parseMsgDirect(MsgDefinBean definBean) {
        MsgDirect direct = null;
        MsgSender sender = definBean.getSenderInfoExt();
        if (sender == null) {
            direct = (definBean.getPublicKey() == null || MemoryDataManager.getInstance().getPubKey().equals(definBean.getPublicKey())) ? MsgDirect.From : MsgDirect.To;
        } else {
            if (TextUtils.isEmpty(sender.publickey)) {
                direct = MemoryDataManager.getInstance().getAddress().equals(sender.address) ? MsgDirect.To : MsgDirect.From;
            } else {
                direct = MemoryDataManager.getInstance().getPubKey().equals(sender.publickey) ? MsgDirect.To : MsgDirect.From;
            }
        }
        return direct;
    }

    /**
     * Update message status
     *
     * @param roomkey
     * @param msgid
     * @param state
     */
    public static void updateMsgSendState(String roomkey, String msgid, int state) {
        MessageEntity msgEntity = MessageHelper.getInstance().loadMsgByMsgid(msgid);
        if (msgEntity != null) {
            msgEntity.setSend_status(state);
            MessageHelper.getInstance().updateMsg(msgEntity);
        }

        if (TextUtils.isEmpty(roomkey)) {
            Map<String, Object> failMap = FailMsgsManager.getInstance().getFailMap(msgid);
            if (failMap != null) {
                Object object = failMap.get("EXT");
                if (object instanceof MsgSendBean) {
                    MsgNoticeBean.sendMsgNotice(MsgNoticeBean.NtEnum.MSG_SEND_SUCCESS, failMap.get("EXT"));
                }
            }
        } else {
            boolean chatAcyRun = ActivityUtil.isRunChatActivity(ChatActivity.class.getName());
            if (chatAcyRun) {
                RecExtBean.sendRecExtMsg(RecExtBean.ExtType.MSGSTATE, roomkey, msgid, state);
            }
        }
    }

    /**
<<<<<<< HEAD
     * Display the list in the session
     *
     * @return
     */
    public static String showContentTxt(int type, MsgDefinBean definBean) {
        String content = "";
        switch (definBean.getType()) {
            case 1://text
                content = definBean.getContent();
                break;
            case 2://voice
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Audio);
                break;
            case 3://picture
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Picture);
                break;
            case 4://video
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Video);
                break;
            case 5://expression
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Expression);
                break;
            case 11://burn message
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Snapchat);
                break;
            case 12://burn back
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Snapchat);
                break;
            case 14:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Funding);
                break;
            case 15:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Transfer);
                break;
            case 16:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Red_packet);
                break;
            case 17:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Location);
                break;
            case 18:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Visting_card);
                break;
            case 23:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Group_Namecard);
                break;
            case 24:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Group_certification);
                break;
            default:
                content = BaseApplication.getInstance().getBaseContext().getString(R.string.Chat_Tips);
                break;
        }

        switch (type) {
            case 0:
                break;
            case 1://show group member nickname
                MsgSender msgSender = definBean.getSenderInfoExt();
                if (msgSender != null && !msgSender.publickey.equals(MemoryDataManager.getInstance().getPubKey())) {
                    content = msgSender.username + ": " + content;
                }
                break;
            case 2:
                break;
        }
        return content;
    }

    /**
     * expression to text
     *
     * @param content
     * @return
     */
    public static SpannableStringBuilder txtTransEmotion(final Context context, final String content) {
        SpannableStringBuilder mSpannableString = new SpannableStringBuilder(content);
        Matcher emjMatcher = Pattern.compile(RegularUtil.VERIFYCATION_EMJ).matcher(content);
        while (emjMatcher.find()) {
            int start = emjMatcher.start();
            int end = emjMatcher.end();
            String emot = content.substring(start, end);
            emot = emot.substring(1, emot.length() - 1) + ".png";
            String key = StickerCategory.emojiMaps.get(emot);
            if (!TextUtils.isEmpty(key)) {
                emot = key;
                Drawable d = ResourceUtil.getEmotDrawable(emot);
                if (d != null) {
                    ImageSpan span = new ImageSpan(d);
                    mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return mSpannableString;
    }

    public static NormalChat loadBaseChat(String pubkey) {
        NormalChat normalChat = null;

        if ((BaseApplication.getInstance().getString(R.string.app_name)).equals(pubkey)) {
            normalChat = RobotChat.getInstance();
        } else {
            GroupEntity groupEntity = ContactHelper.getInstance().loadGroupEntity(pubkey);
            if (groupEntity != null) {
                normalChat = new GroupChat(groupEntity);
            } else {
                ContactEntity friendEntity = ContactHelper.getInstance().loadFriendEntity(pubkey);
                if (friendEntity != null) {
                    normalChat = new FriendChat(friendEntity);
                }
            }
        }
        return normalChat;
    }
}
