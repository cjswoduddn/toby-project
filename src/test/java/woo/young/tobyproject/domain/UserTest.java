package woo.young.tobyproject.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;

    @BeforeEach
    void before(){
        user = new User();
    }

    @Test
    void upgradeLevel(){
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.getNext() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            Assertions.assertEquals(user.getLevel(), level.getNext());
        }
    }
    @Test
    public void exupgrade() throws Exception{
        user.setLevel(Level.GOLD);
        Assertions.assertThrows(IllegalStateException.class, ()->user.upgradeLevel());
    }

}