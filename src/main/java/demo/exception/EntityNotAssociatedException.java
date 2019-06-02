package demo.exception;

import demo.entity.persistent.HibernateEntity;

public class EntityNotAssociatedException extends RuntimeException {

    public <T extends HibernateEntity, V extends HibernateEntity> EntityNotAssociatedException
            (Class<T> clazz, Long id, Class<V> childClazz, Long childId) {
        super(childClazz.getSimpleName() + "{" + childId + "}"
                + " is not associated with " + clazz.getSimpleName() + "{" + id + "}");
    }
}
