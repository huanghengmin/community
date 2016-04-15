package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.LardLordDao;
import com.hzih.community.domain.LardLord;
import com.hzih.community.domain.Tenant;
import com.hzih.community.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


public class LardLordDaoImpl extends MyDaoSupport implements LardLordDao {

    @Override
    public void setEntityClass() {
        this.entityClass = LardLord.class;
    }

    @Override
    public PageResult find(int start, int limit, long community_doorplate_id) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from LardLord s where 1=1");
        List params = new ArrayList(1);// 手动指定容量，避免多次扩容

        sb.append(" and s.communityDoorplate.id = ?");
        params.add(community_doorplate_id);

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public PageResult find(int start, int limit, String communityId, String buildId, String unitId, String doorplate, String room, String name, String idCard, String phone,int type) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from LardLord s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(communityId)) {
            sb.append(" and s.communityRoom.communityDoorplate.communityBuildUnit.communityBuild.community.id = ?");
            params.add(Long.parseLong(communityId));
        }
        if (StringUtils.isNotBlank(buildId)) {
            sb.append(" and s.communityRoom.communityDoorplate.communityBuildUnit.communityBuild.id = ?");
            params.add(Long.parseLong(buildId));
        }

        if (StringUtils.isNotBlank(unitId)) {
            sb.append(" and s.communityRoom.communityDoorplate.communityBuildUnit.id = ?");
            params.add(Long.parseLong(unitId));
        }

        if (StringUtils.isNotBlank(doorplate)) {
            sb.append(" and s.communityRoom.communityDoorplate.doorplate like '%"+doorplate+"%'");
//            params.add("%" + doorplate + "%");
        }

        if (StringUtils.isNotBlank(room)) {
            sb.append(" and s.communityRoom.room = ?");
            params.add(Integer.valueOf(room));
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append(" and s.name = ?");
            params.add("%"+name+"%");
        }
        if (StringUtils.isNotBlank(idCard)) {
            sb.append(" and s.idCard = ?");
            params.add("%" + idCard + "%");
        }
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and s.phone = ?");
            params.add("%" + phone +"%");
        }

        if(type==1) {
            sb.append("and s.type <= ?");
            params.add(type);
        } else if(type==2) {
            sb.append("and s.type = ?");
            params.add(type);
        } else if(type==20) {
            sb.append("and s.attention > 1");
        } else {

        }

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public PageResult find(int start, int limit, long id, String name, String idCard) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from LardLord s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and s.name like ?");
            params.add("%"+name+"%");
        }
        if (StringUtils.isNotBlank(idCard)) {
            sb.append(" and s.idCard like ?");
            params.add("%"+idCard+"%");
        }

        sb.append(" and s.communityDoorplate.communityBuildUnit.communityBuild.community.id = ?");
        params.add(id);

        String countString = "select count(*) " + sb.toString();
        String queryString = sb.toString();

        PageResult ps = this.findByPage(queryString, countString, params.toArray(), pageIndex, limit);
        logger.debug(ps == null ? "ps=null" : "ps.results.size:"
                + ps.getResults().size());
        return ps;
    }

    @Override
    public LardLord find(int type_ocr) {
        StringBuffer sb = new StringBuffer(" from LardLord s where 1=1 and s.ocr='" + type_ocr + "' order by id desc");
        Query query = getSession().createQuery(sb.toString());
        query.setMaxResults(1);
        List<LardLord> expressLogs = query.list();
        if(expressLogs!=null&&expressLogs.size()>0) {
            return expressLogs.get(0);
        }else {
            return null;
        }
    }

    @Override
    public LardLord findByIdCard(long id, String idCard) {
        String hql = new String("from LardLord s where s.idCard='" + idCard+"' and s.communityDoorplate.id ="+id);
        List<LardLord> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public LardLord findById(long id) {
        String hql = new String("from LardLord where id=" + id);
        List<LardLord> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(LardLord lardLord) {
        try {
            getHibernateTemplate().delete(lardLord);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean add(LardLord lardLord) {
        try {
            getHibernateTemplate().save(lardLord);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean modify(LardLord lardLord) {
        try {
            getHibernateTemplate().saveOrUpdate(lardLord);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List findByeIds(String ids) {
        String hql = "from LardLord where id in("+ids+")";
        List<LardLord> list = getHibernateTemplate().find(hql);
       return list;
    }

    @Override
    public boolean clearLardLord() {
        boolean flag =false;
        String sq="update LardLord s set s.status= "+0+" where s.status = 1";
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query_sq=session.createQuery(sq);
            query_sq.executeUpdate();
            session.getTransaction().commit();
            flag=true;

        } catch (Exception e){

        } finally {
            session.close();
        }
        return flag;
    }
}
