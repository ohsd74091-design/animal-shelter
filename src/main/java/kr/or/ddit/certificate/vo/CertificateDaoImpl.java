package kr.or.ddit.certificate.vo;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.certificate.dao.ICertificateDao;
import kr.or.ddit.util.MybatisUtil;

public class CertificateDaoImpl implements ICertificateDao {
	private static ICertificateDao dao = new CertificateDaoImpl();

    public static ICertificateDao getInstance() {
        return dao;
    }
    
    
	@Override
	public AdoptionCertificateVO selectCertificate(Map<String, Object> param) {
		SqlSession session = MybatisUtil.getsqlsession();

        AdoptionCertificateVO vo = null;

        try {
            vo = session.selectOne("certificate.selectAdoptionCertificate", param);
        } finally {
            session.close();
        }

        return vo;
    }
}
