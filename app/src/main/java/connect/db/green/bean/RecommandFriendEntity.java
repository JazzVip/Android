package connect.db.green.bean;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "RECOMMAND_FRIEND_ENTITY".
 */
@Entity
public class RecommandFriendEntity implements java.io.Serializable {

    @Id(autoincrement = true)
    private Long _id;

    @NotNull
    @Unique
    private String pub_key;

    @NotNull
    private String username;

    @NotNull
    @Unique
    private String address;

    @NotNull
    private String avatar;
    private Integer status;

    @Generated
    public RecommandFriendEntity() {
    }

    public RecommandFriendEntity(Long _id) {
        this._id = _id;
    }

    @Generated
    public RecommandFriendEntity(Long _id, String pub_key, String username, String address, String avatar, Integer status) {
        this._id = _id;
        this.pub_key = pub_key;
        this.username = username;
        this.address = address;
        this.avatar = avatar;
        this.status = status;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    @NotNull
    public String getPub_key() {
        return pub_key;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPub_key(@NotNull String pub_key) {
        this.pub_key = pub_key;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    @NotNull
    public String getAvatar() {
        return avatar;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAvatar(@NotNull String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
