package com.testSoftcaribbean.treeBPlus.Services;
import java.util.List;

import com.testSoftcaribbean.treeBPlus.Entity.Client;

public interface DataTableService {
    public List<Client> getDataTable(String table);
    public boolean insertDataTable(String table,Client client);
}