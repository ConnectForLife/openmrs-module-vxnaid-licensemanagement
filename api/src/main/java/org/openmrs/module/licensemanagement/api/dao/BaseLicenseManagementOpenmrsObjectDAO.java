/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.licensemanagement.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.util.List;

/**
 * The base class for DAOs in License Management module.
 *
 * @param <E> the main type handled by the DAO
 */
abstract class BaseLicenseManagementOpenmrsObjectDAO<E extends OpenmrsObject> {

    private final Class<E> openmrsObjectClass;

    private DbSessionFactory dbSessionFactory;

    /**
     * @param openmrsObjectClass the Class of the main type handled by the Dao, not null
     */
    BaseLicenseManagementOpenmrsObjectDAO(final Class<E> openmrsObjectClass) {
        this.openmrsObjectClass = openmrsObjectClass;
    }

    /**
     * Return the first result of the {@code criteria} execution or null if there was no results.
     *
     * @param criteria   the criteria to execute, not null
     * @param resultType the result type class, not null
     * @param <T>        the Type of result
     * @return the first result or null if there was no results
     */
    <T extends OpenmrsObject> T firstResult(final Criteria criteria, final Class<T> resultType) {
        final List<?> entities = criteria.list();

        if (entities.isEmpty()) {
            return null;
        }

        return resultType.cast(entities.get(0));
    }

    Class<E> getOpenmrsObjectClass() {
        return openmrsObjectClass;
    }

    public void setDbSessionFactory(final DbSessionFactory dbSessionFactory) {
        this.dbSessionFactory = dbSessionFactory;
    }

    DbSession getSession() {
        return dbSessionFactory.getCurrentSession();
    }

    E internalRead(final Integer id) {
        return openmrsObjectClass.cast(getSession().get(openmrsObjectClass, id));
    }

    E internalReadByUuid(final String uuid) {
        return openmrsObjectClass.cast(
                getSession().createCriteria(openmrsObjectClass).add(Restrictions.eq("uuid", uuid)).uniqueResult());
    }

    E internalSave(final E obj) {
        getSession().saveOrUpdate(obj);
        return obj;
    }

    void internalDelete(final E obj) {
        getSession().delete(obj);
    }
}
