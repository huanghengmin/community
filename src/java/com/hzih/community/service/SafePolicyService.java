package com.hzih.community.service;

import com.hzih.community.domain.SafePolicy;

public interface SafePolicyService {

	SafePolicy getData();

    public String select() throws Exception;

    public String update(SafePolicy safePolicy) throws Exception;

    public String selectPasswordRules() throws Exception;
}
