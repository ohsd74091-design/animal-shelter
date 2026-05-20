package kr.or.ddit.certificate.service;

import java.util.HashMap;
import java.util.Map;

import kr.or.ddit.certificate.dao.ICertificateDao;
import kr.or.ddit.certificate.vo.AdoptionCertificateVO;
import kr.or.ddit.certificate.vo.CertificateDaoImpl;

public class CertificateServiceImpl implements ICertificateService {
	 private static ICertificateService service = new CertificateServiceImpl();
	    private ICertificateDao dao;

	    private CertificateServiceImpl() {
	        dao = CertificateDaoImpl.getInstance();
	    }
	    public static ICertificateService getInstance() {
	        return service;
	    }
	    
	    
	    public AdoptionCertificateVO getCertificate(int adoptionId, String memberId) {

	        Map<String, Object> param = new HashMap<>();
	        param.put("adoptionId", adoptionId);
	        param.put("memberId", memberId);

	        return dao.selectCertificate(param);
	    }
	    
	    
	    
	    
}
