package com.testSoftcaribbean.treeBPlus.Services;
import java.util.List;

import com.testSoftcaribbean.treeBPlus.Entity.Client;
import com.testSoftcaribbean.treeBPlus.TreeBplus.BTreePlus;

public interface TreeBPlusService {
    public BTreePlus initialize(int degree,List<Client> listClient);
}