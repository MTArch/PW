package com.parserlabs.phr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.model.ShareCMRequestPlayLoad;
import com.parserlabs.phr.proxy.CmProxy;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CustomSpanned
public class NotifyIntegratedProgramService {

	@Autowired
	private CmProxy cmProxy;

	@Async
	public void shareProfile(ShareCMRequestPlayLoad shareCMRequestPlayLoad) {

		try {
			if (StringUtils.hasLength(shareCMRequestPlayLoad.getHealthId())) {
				cmProxy.sharePHRProfile(shareCMRequestPlayLoad);
				log.info("SuccessFully sent update to  cm {}, {}.", shareCMRequestPlayLoad.getHealthId(),
						shareCMRequestPlayLoad.getHealthId());
			}
		} catch (Exception exe) {
					log.info("possible error while sending data to cm {}, {}.", shareCMRequestPlayLoad.getHealthId(),
					shareCMRequestPlayLoad.getHealthId());
		}

	}

}
