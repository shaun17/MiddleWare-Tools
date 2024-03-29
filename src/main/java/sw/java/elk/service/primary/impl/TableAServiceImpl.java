package sw.java.elk.service.primary.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.java.elk.dao.TableADao;
import sw.java.elk.po.TableA;
import sw.java.elk.service.primary.TableAService;

import java.util.List;
import java.util.Map;

@Service
public class TableAServiceImpl implements TableAService {

    @Autowired
    TableADao tableADao;

    @Override
    public List<Map<String, Object>> queryList() {
        return tableADao.queryList();
    }

    @Override
    public int insertOne(TableA tableA) {
        return tableADao.insert(tableA);
    }
}
