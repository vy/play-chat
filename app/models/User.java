package models;

import net.sf.oval.constraint.NotNull;
import play.data.binding.NoBinding;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class User extends Model {
    @NotNull
    @Required
    @Unique
    public String fbid;

    @NotNull
    @Required
    public String name;

    @NoBinding
    public boolean isAdmin;

    public User(String fbid, String name) {
        this.fbid = fbid;
        this.name = name;
        this.isAdmin = false;
    }

    public static User findByFBId(String fbid) {
        return find("fbid", fbid).first();
    }

    @Override
    public String toString() {
        return String.format("User[%s]", name);
    }
}
