package kr.or.ddit.certificate.service;

import kr.or.ddit.certificate.vo.AdoptionCertificateVO;

public interface ICertificateService {
	
	AdoptionCertificateVO getCertificate(int adoptionId, String memberId);

}
