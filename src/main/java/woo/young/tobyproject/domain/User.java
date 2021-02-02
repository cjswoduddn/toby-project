package woo.young.tobyproject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    String id;
    String name;
    String password;

    Level level;
    int login;
    int recommend;

    public User() {
    }

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel(){
        Level next = level.getNext();
        if(next == null) throw new IllegalStateException("다음 레벨은 정의되어 있지 않아요");
        level = next;
    }
}
