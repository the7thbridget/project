package edu.gatech.cs6310.groceryexpress.service;

import edu.gatech.cs6310.groceryexpress.common.R;

public interface ArchiveService {
    R archiveDataBasedOnCriteria();
    Boolean archiveCustomer(Integer timePeriod);
    Boolean archiveOrder(Integer timePeriod);
    //R displayArchivedCustomer();
    //R displayArchivedOrder();
    R retrieveArchivedCustomer();
    R retrieveArchivedOrder();
    Boolean purgeArchivedCustomer(Integer archivePeriod);
    Boolean purgeArchivedOrder(Integer archivePeriod);
}
