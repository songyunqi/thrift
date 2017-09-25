package com.foo.thrift;

import org.apache.thrift.TException;

import java.util.HashMap;
import java.util.Map;

public class ProfileServiceImpl implements ProfileService.Iface {
    /*
     * (non-Javadoc)
     *
     * @see
     * org.zlex.support.thrift.ProfileService.Iface#updateName(org.zlex.support
     * .thrift.Profile, java.lang.String)
     */
    @Override
    public String updateName(Profile profile, String name) throws TException {
        profile.setName(name);
        return profile.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zlex.support.thrift.ProfileService.Iface#updateScore(org.zlex.support
     * .thrift.Profile, int)
     */
    @Override
    public int updateScore(Profile profile, int score) throws TException {
        profile.setScore(profile.getScore() + score);
        return profile.getScore();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zlex.support.thrift.ProfileService.Iface#toMap(org.zlex.support.thrift
     * .Profile)
     */
    @Override
    public Map<String, String> toMap(Profile profile) throws TException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", profile.getName());
        map.put("score", "" + profile.getScore());
        map.put("isEnable", "" + profile.isEnable());
        return map;
    }
}
