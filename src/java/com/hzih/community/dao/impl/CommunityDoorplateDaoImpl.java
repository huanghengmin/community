package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityDoorplateDao;
import com.hzih.community.domain.CommunityDoorplate;
import com.hzih.community.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


public class CommunityDoorplateDaoImpl extends MyDaoSupport implements CommunityDoorplateDao {

    @Override
    public void setEntityClass() {
        this.entityClass = CommunityDoorplate.class;
    }

    @Override
    public PageResult find(int start, int limit,String community_id,String community_build_id,String community_build_unit_id,String doorplate) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityDoorplate s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityBuildUnit.communityBuild.community.id = ?");
            params.add(Long.parseLong(community_id));
        }
        if (StringUtils.isNotBlank(community_build_id)) {
            sb.append(" and s.communityBuildUnit.communityBuild.id = ?");
            params.add(Long.parseLong(community_build_id));
        }

        if (StringUtils.isNotBlank(community_build_unit_id)) {
            sb.append(" and s.communityBuildUnit.id = ?");
            params.add(Long.parseLong(community_build_unit_id));
        }

        if (StringUtils.isNotBlank(doorplate)) {
            sb.append(" and s.doorplate like ?");
            params.add("%"+doorplate+"%");
        }
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public PageResult find(int start, int limit, String community_id) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityDoorplate s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityBuildUnit.communityBuild.community.id = ?");
            params.add(Long.parseLong(community_id));
        }
        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public PageResult find(int start, int limit) {
        int pageIndex = start / limit + 1;
        String hql = " from CommunityDoorplate where 1=1";
        String countHql = "select count(*) " + hql;
        PageResult ps = findByPage(hql, countHql, pageIndex, limit);
        return ps;
    }

    @Override
    public CommunityDoorplate findById(long id) {
        String hql = new String(" from CommunityDoorplate where id=" + id);
        List<CommunityDoorplate> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean add(CommunityDoorplate companyPoint) {
        try {
            getHibernateTemplate().saveOrUpdate(companyPoint);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean remove(long id) {
        try {
           getHibernateTemplate().delete(new CommunityDoorplate(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CommunityDoorplate> find(String community_id, String build, String unit, String doorplate) {
        StringBuffer sb = new StringBuffer(" from CommunityDoorplate s where 1=1");
        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityBuildUnit.communityBuild.community.id = " + community_id);
        }
        if (StringUtils.isNotBlank(build)) {
            sb.append(" and s.communityBuildUnit.communityBuild.id = "+ build);
        }
        if (StringUtils.isNotBlank(unit)) {
            sb.append(" and s.communityBuildUnit.id = "+ unit);
        }

        if (StringUtils.isNotBlank(doorplate)) {
            sb.append(" and s.doorplate = "+ doorplate);
        }
        List<CommunityDoorplate> list = getHibernateTemplate().find(sb.toString());
        return list;
    }

    @Override
    public boolean czDoorplate(String id) {
        boolean flag =false;
        String s="update CommunityDoorplate s set s.status= "+1+" where s.id="+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query=session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag=true;

        } catch (Exception e){

        } finally {
            session.close();
        }
        return flag;
    }

    @Override
    public boolean zzDoorplate(String id) {
        boolean flag =false;
        String s="update CommunityDoorplate s set s.status= "+0+" where s.id="+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query=session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag=true;

        } catch (Exception e){

        } finally {
            session.close();
        }
        return flag;
    }
}
