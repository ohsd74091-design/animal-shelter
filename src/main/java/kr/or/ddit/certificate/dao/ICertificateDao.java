package kr.or.ddit.certificate.dao;

import java.util.Map;

import kr.or.ddit.certificate.vo.AdoptionCertificateVO;

public interface ICertificateDao {
	AdoptionCertificateVO selectCertificate(Map<String, Object> param);
}
