// =============================================================================
// THALES COMMUNICATIONS & SECURITY
//
// Copyright (c) THALES 2014 All rights reserved.
// This file and the information it contains are property of THALES COMMUNICATIONS &
// SECURITY and confidential. They shall not be reproduced nor disclosed to any
// person except to those having a need to know them without prior written
// consent of COMMUNICATIONS & SECURITY .
//
package com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.DataModuleAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.sort.PageChangeEvent.PageChangeEventListener;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.sort.finder.DichotomieEntityPositionFinder;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.sort.finder.EntityPositionFinder;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.sort.sorter.Sorter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.notification.NotificationElement;

/**
 * The {@link SortedPageDataModule} is a
 * {@link com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.module.DataModule
 * DataModule} which sort the entity according to criterion
 *  and send a {@link PageChangeEvent} if the content of the set page changed.
 */
public class SortedPageDataModule extends DataModuleAbstract {

    /** Class used to find the position of an entity into the container. */
    private final EntityPositionFinder finder_ = new DichotomieEntityPositionFinder();

    /** list of sorted entity id */
    private List<String> indexedEntity_ = new ArrayList<String>();

    /** reverse entity index */
    private Map<String, Integer> indexByEntityId_ = new HashMap<String, Integer>();

    /** entity cache need to compute index */
    private Map<String, EntityClient> localCache_ = new HashMap<String, EntityClient>();

    /** The current ordering. */
    private Ordering<EntityClient> ordering_;

    /** Set of sorted attribute name. */
    private Set<String> sortedAttributes_  = new HashSet<String>();

    /** start index of the page */
    private int pageStartIndex_ = 0;

    /** page size */
    private int pageSize_ = 0;

    /** page change listener */
    private PageChangeEventListener listener_;

    private boolean pageMustBeUpdated_ = false;

    private Set<Integer> modifiedIndex_ = new HashSet<Integer>();
    private Set<EntityClient> toDelete_ = new HashSet<EntityClient>();
    private Set<EntityClient> toInsert_ = new HashSet<EntityClient>();

    private boolean synchronization_ = false;

    /**
     * Set the page.
     * @param startIndex the page starting index.
     * @param pageSize the size of the page.
     */
    public void setPage(final int startIndex, final int pageSize) {
        pageStartIndex_ = startIndex;
        pageSize_ = pageSize;

        notifyListener(true);
    }

    /**
     * Set the {@link PageChangeEventListener} which will be notified when the page content changed.
     * @param listener the listener.
     */
    public void setPageChangeListener(final PageChangeEventListener listener) {
        listener_ = listener;
        notifyListener(true);
    }

    /**
     * Set the sorters. The list of sorters will be compound.
     * @param sorters the sorters list.
     */
    public void setSorters(final List<Sorter> sorters) {
        sortedAttributes_ = new HashSet<String>(sorters.size());

        Ordering<EntityClient> ordering = sorters.get(0).getOrdering();
        sortedAttributes_.add(sorters.get(0).getAttributeName());

        // compound the ordering
        for (int i = 1; i < sorters.size(); i++) {
            ordering = ordering.compound(sorters.get(i).getOrdering());
            sortedAttributes_.add(sorters.get(i).getAttributeName());
        }

        ordering_ = ordering;

        // refresh the whole list
        refreshList();

        // notify listeners
        notifyListener(true);
    }

    /**
     * Remove the sorters.
     */
    public void removeSorters() {
        ordering_ = null;
        sortedAttributes_.clear();

        // refresh the whole list
        refreshList();

        // notify listeners
        notifyListener(true);
    }

    /**
     * Refresh the list according its sorter.
     */
    private void refreshList() {

        if (ordering_ != null) {
            // sort according the current ordering
            // use classic collection merge sort

            Collections.sort(indexedEntity_, new Comparator<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public int compare(final String o1, final String o2) {
                    return ordering_.compare(localCache_.get(o1), localCache_.get(o2));
                }
            });

        }

        // refresh the index
        refreshIndex();

    }

    private void refreshIndex() {
        refreshIndex(0, localCache_.size() - 1);
    }

    private void refreshIndex(final Integer start, final Integer end) {

        for (int i = start; i <= end; i++) {
            indexByEntityId_.put(indexedEntity_.get(i), i);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotify(final List<NotificationElement> notifications) {
        pageMustBeUpdated_ = false;

        if (synchronization_) {
            resetSortedPage(notifications);
        } else {
            updateSortedPage(notifications);
        }

    }

    private void updateSortedPage(final List<NotificationElement> notifications) {
        for (final NotificationElement notificationElement : notifications) {

            switch (notificationElement.getType()) {
                case INSERT:
                    prepareInsertBatch(notificationElement.getEntity());
                    break;
                case UPDATE:
                    prepareUpdateBatch(notificationElement.getEntity());
                    break;
                case DELETE:
                    prepareDeleteBatch(notificationElement.getEntity());
                    break;
                default:
                    break;
            }

        }

        processModification();

        notifyListener(false);

        // relay the notification
        super.onNotify(notifications);
    }

    private void resetSortedPage(final List<NotificationElement> notifications) {
        indexedEntity_.clear();
        indexByEntityId_.clear();

        for (final NotificationElement notificationElement : notifications) {

            final EntityClient entity = notificationElement.getEntity();

            switch (notificationElement.getType()) {
                case INSERT:
                    localCache_.put(entity.getId(), entity);
                    break;
                case UPDATE:
                    break;
                case DELETE:
                    localCache_.remove(entity.getId());
                    break;
                default:
                    break;
            }

        }

        // rebuild the indexed entity list
        for (final String id : localCache_.keySet()) {
            indexedEntity_.add(id);
        }

        refreshList();

        // force the page update when synchronizing
        notifyListener(true);

        synchronization_ = false;
        // relay the notification
        super.onNotify(notifications);
    }

    private void prepareInsertBatch(final EntityClient entity) {
        toInsert_.add(getEntity(entity.getId()));
    }

    private void prepareUpdateBatch(final EntityClient entity) {
        if (checkIfEntityPositionMustBeUpdated(entity)) {
            toInsert_.add(getEntity(entity.getId()));
            toDelete_.add(getEntity(entity.getId()));
        } else {
            // if the entity is currently in the page, set flag to be sure that the page will be updated.
            final Range<Integer> pageRange = Ranges.closedOpen(pageStartIndex_, pageSize_  + pageStartIndex_);
            final Integer currentPosition = indexByEntityId_.get(entity.getId());
            if (pageRange.contains(currentPosition)) {
                pageMustBeUpdated_ = true;
            }
        }

    }

    private void prepareDeleteBatch(final EntityClient entity) {
        toDelete_.add(localCache_.get(entity.getId()));
    }

    private void processModification() {

        // delete before insert
        processDelete();
        processInsert();

        refreshReverseIndex();

    }

    private void processDelete() {

        final Set<String> idsToClean = new HashSet<String>(toDelete_.size());
        for (final EntityClient entity : toDelete_) {
            //It can happen that the entity is not indexed so the index is null
            final Integer currentIndex = indexByEntityId_.remove(entity.getId());

            // prepare a batch for indexed entity as arraylist can have poor performance on remove with huge size
            // compute real index, takes in account the other deleted entities when computes the index
            idsToClean.add(entity.getId());

            localCache_.remove(entity.getId());

            //avoid to propagate the null information
            if (currentIndex != null) {
                modifiedIndex_.add(currentIndex);
            }

        }
        
        //cleanup the list now for performances
        indexedEntity_.removeAll(idsToClean);
    }

    private void processInsert() {

        for (final EntityClient entity: toInsert_) {
            localCache_.put(entity.getId(), entity);

            // get sorted index
            final int insertionIndex = computeEntityPosition(entity);
            modifiedIndex_.add(insertionIndex);

            if (insertionIndex == localCache_.size()) {
                indexedEntity_.add(entity.getId());
            } else {
                indexedEntity_.add(insertionIndex, entity.getId());
            }

            indexByEntityId_.put(entity.getId(), insertionIndex);
        }

    }

    /**
     * refresh the
     */
    private void refreshReverseIndex() {

        if (!modifiedIndex_.isEmpty()) {
            Integer startIndex = null;

            final Range<Integer> pageRange = Ranges.closedOpen(pageStartIndex_, pageStartIndex_ + pageSize_);

            for (final Integer currentIndex : modifiedIndex_) {
                // first iteration
                if (startIndex == null /*&& endIndex == null*/) {
                    startIndex = currentIndex;
                } else {
                    // cannot be null so suppress warning
                    startIndex = Math.min(startIndex, currentIndex);
                }

                // check if the page contains a modified entity, only if the update was not already needed
                if (pageMustBeUpdated_ == false && pageRange.contains(currentIndex)) {
                    pageMustBeUpdated_ = true;
                }

            }

            // secured index just to be sure
            // start index cannot be null
            startIndex = Math.max(0, startIndex);

            // refresh index
            refreshIndex(startIndex, localCache_.size() - 1);

            modifiedIndex_.clear();
            toDelete_.clear();
            toInsert_.clear();

        }

    }

    /**
     * Return true if the modification could change the entity position into the view
     * @param entityUpdate the update
     * @return <code>true</code> if the entity position in the container is likely to be changed
     */
    private boolean checkIfEntityPositionMustBeUpdated(final EntityClient entityUpdate) {
        final Set<String> fieldToUpdate = entityUpdate.attributeNames();

        boolean modifyIndex = false;

        for (final String attributeName : fieldToUpdate) {

            // if the update could modify the entity index in the view
            if (sortedAttributes_.contains(attributeName)) {
                modifyIndex = true;
                break;
            }

        }

        return modifyIndex;
    }

    /**
     * Compute the entity position into the container/
     * @param entity the entity to position.
     * @return the position of the entity.
     */
    private int computeEntityPosition(final EntityClient entity) {
        return finder_.computeEntityPosition(localCache_, indexedEntity_, ordering_, entity);
    }

    private void notifyListener(final boolean forceEvent) {

        final boolean updatePossible = listener_ != null;
        final boolean pageUpdateNeeded = forceEvent || pageMustBeUpdated_;

        if (updatePossible && pageUpdateNeeded) {

            int pageEndIndex = pageStartIndex_ + pageSize_ - 1;
            final int totalSize = localCache_.size();

            // be sure to be into the bound
            if (pageEndIndex >= totalSize) {
                pageEndIndex = totalSize - 1;
                pageStartIndex_ = Math.max(pageEndIndex - pageSize_ + 1, 0);
            }

            final int pageContentSize = pageEndIndex - pageStartIndex_ + 1;

            final List<EntityClient> page = new ArrayList<EntityClient>(pageContentSize);

            // creates the page event with full entities from cache.
            for (int i = pageStartIndex_; i <= pageEndIndex; i++) {
                page.add(getEntity(indexedEntity_.get(i)));
            }

            listener_.onPageChange(new PageChangeEvent(pageStartIndex_, page));

        }

    }

    /**
     * Get the index of an entity into the data module.
     * @param entityId the entity id.
     * @return the index of the entity.
     */
    public Integer getEntityIndex(final String entityId) {
        return indexByEntityId_.get(entityId);
    }

    /**
     * Prepare the sorted page data module to be synchronized.
     * The next notification will be processed with a simpler (but faster in this use case) algorithm than the normal one.
     */
    public void synchronization() {
        synchronization_ = true;
    }

}
