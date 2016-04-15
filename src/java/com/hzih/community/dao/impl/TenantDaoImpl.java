package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.TenantDao;
import com.hzih.community.domain.LardLord;
import com.hzih.community.domain.Tenant;
import com.hzih.community.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


public class TenantDaoImpl extends MyDaoSupport implements TenantDao {

    @Override
    public void setEntityClass() {
        this.entityClass = Tenant.class;
    }

    @Override
    public PageResult find(int start, int limit, long community_doorplate_id) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from Tenant s where 1=1");
        List params = new ArrayList(1);// 手动指定容量，避免多次扩容

        sb.append(" and s.communityRoom.communityDoorplate.id = ?");
        params.add(community_doorplate_id);

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public PageResult find(int start, int limit,long community_id, String name, String idCard) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from Tenant s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and s.name like ?");
            params.add("%"+name+"%");
        }
        if (StringUtils.isNotBlank(idCard)) {
            sb.append(" and s.idCard like ?");
            params.add("%"+idCard+"%");
        }

        sb.append(" and s.communityRoom.communityDoorplate.communityBuildUnit.communityBuild.community.id = ?");
        params.add(community_id);

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public Tenant findById(long id) {
        String hql = new String("from Tenant where id=" + id);
        List<Tenant> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(Tenant tenant) {
        try {
            getHibernateTemplate().delete(tenant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean add(Tenant tenant) {
        try {
            getHibernateTemplate().save(tenant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean modify(Tenant tenant) {
        try {
            getHibernateTemplate().saveOrUpdate(tenant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Tenant findByIdCard(String communityDoorplateId, String idCard, int status) {
        String hql = new String(" from Tenant s where s.idCard = '" + idCard
                + "' and s.communityRoom.communityDoorplate.id = " + Long.parseLong(communityDoorplateId))
                + " and s.status = " + status;
        List<Tenant> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List findByeIds(String ids) {
        String hql = "from Tenant where id in("+ids+")";
        List<Tenant> list = getHibernateTemplate().find(hql);
        return list;
    }

    @Override
    public Tenant find(int type_ocr) {
        StringBuffer sb = new StringBuffer(" from Tenant s where 1=1 and s.ocr='" + type_ocr + "' order by id desc");
        Query query = getSession().createQuery(sb.toString());
        query.setMaxResults(1);
        List<Tenant> expressLogs = query.list();
        if(expressLogs!=null&&expressLogs.size()>0) {
            return expressLogs.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Tenant findByIdCard(long id, String idCard) {
        String hql = new String(" from Tenant s where s.idCard = '" + idCard
                + "' and s.communityRoom.communityDoorplate.id = " + id);
        List<Tenant> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
