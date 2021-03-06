package cn.itcast.core.service;

import cn.itcast.common.utils.POIUtil;
import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    //查询所有
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //查询
        Page<Brand> p = (Page<Brand>) brandDao.selectByExample(null);


        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        //分页插件
        PageHelper.startPage(pageNum, pageSize);

        BrandQuery brandQuery = new BrandQuery();

        BrandQuery.Criteria criteria = brandQuery.createCriteria();

        //判断品牌名称   "   "
        if (null != brand.getName() && !"".equals(brand.getName().trim())) {
            criteria.andNameLike("%" + brand.getName().trim() + "%");
        }
        //判断品牌首字母
        if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        //查询
        Page<Brand> p = (Page<Brand>) brandDao.selectByExample(brandQuery);

        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }


    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);

             /*   insert into tb_brand (id,name,100) values (null,宝马,B,null,null,...); 100+
                insert into tb_brand (name,firstChar) values (宝马,B)  好*/
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Long[] ids) {

        BrandQuery brandQuery = new BrandQuery();
        brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
        brandDao.deleteByExample(brandQuery);
    }

    // excel导入数据库 szj
    @Override
    public void importBrand(String[] string) throws Exception {
//        if (null != string && 1 < string.length){
        String brandName = string[0];
        String firstChar = string[1];
        Brand brand = new Brand();
        brand.setName(brandName);
        brand.setFirstChar(firstChar);
        add(brand);
    }

    // 批量更新审核状态 szj
    @Override
    public void commit(Long[] ids) {
        Brand brand = new Brand();
        brand.setStatus("1");
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        brandDao.updateByExampleSelective(brand,brandQuery);
    }
}