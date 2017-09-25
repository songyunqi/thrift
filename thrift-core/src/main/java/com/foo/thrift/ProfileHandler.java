package com.foo.thrift;

import org.apache.thrift.TException;

import java.util.Map;

public class ProfileHandler implements ProfileService.Iface {

    @Override
    public String updateName(Profile profile, String name) throws TException {
        return null;
    }

    @Override
    public int updateScore(Profile profile, int score) throws TException {
        return 0;
    }

    @Override
    public Map<String, String> toMap(Profile profile) throws TException {
        return null;
    }

}
