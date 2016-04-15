package com.hzih.community.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.community.dao.CommunityRoomDao;
import com.hzih.community.domain.CommunityRoom;
import com.hzih.community.utils.StringUtils;
import com.hzih.community.web.action.net.InterfaceManagerAction;

import java.util.ArrayList;
import java.util.List;


public class CommunityRoomDaoImpl extends MyDaoSupport implements CommunityRoomDao {

    @Override
    public void setEntityClass() {
        this.entityClass = CommunityRoom.class;
    }

    @Override
    public PageResult find(int start, int limit,String community_id,String community_build_id,String community_build_unit_id,String doorplate,String room) {
        int pageIndex = start / limit + 1;
        StringBuffer sb = new StringBuffer(" from CommunityRoom s where 1=1");
        List params = new ArrayList();// 手动指定容量，避免多次扩容

        if (StringUtils.isNotBlank(community_id)) {
            sb.append(" and s.communityDoorplate.communityBuildUnit.communityBuild.community.id = ?");
            params.add(Long.parseLong(community_id));
        }
        if (StringUtils.isNotBlank(community_build_id)) {
            sb.append(" and s.communityDoorplate.communityBuildUnit.communityBuild.id = ?");
            params.add(Long.parseLong(community_build_id));
        }

        if (StringUtils.isNotBlank(community_build_unit_id)) {
            sb.append(" and s.communityDoorplate.communityBuildUnit.id = ?");
            params.add(Long.parseLong(community_build_unit_id));
        }

        if (StringUtils.isNotBlank(doorplate)) {
            sb.append(" and s.communityDoorplate.id = ?");
            params.add(Long.parseLong(doorplate));
        }

        if (StringUtils.isNotBlank(room)) {
            sb.append(" and s.communityRoom = ?");
            params.add(Integer.valueOf(room));
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
        StringBuffer sb = new StringBuffer(" from CommunityRoom s where 1=1");
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
        String hql = " from CommunityRoom where 1=1";
        String countHql = "select count(*) " + hql;
        PageResult ps = findByPage(hql, countHql, pageIndex, limit);
        return ps;
    }

    @Override
    public CommunityRoom findById(long id) {
        String hql = new String(" from CommunityRoom where id=" + id);
        List<CommunityRoom> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean add(CommunityRoom companyPoint) {
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
           getHibernateTemplate().delete(new CommunityRoom(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CommunityRoom findByRoom(long id, String room) {
        String hql = new String(" from CommunityRoom s where room=" + room+" and s.communityDoorplate.id="+id);
        List<CommunityRoom> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
