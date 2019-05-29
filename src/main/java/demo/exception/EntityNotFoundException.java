package demo.exception;

import demo.entity.HibernateEntity;

public class EntityNotFoundException extends RuntimeException {

    public <T extends HibernateEntity> EntityNotFoundException(Class<T> clazz, Long id) {
        super("Entity not found for class: " + clazz.getSimpleName() + " and id: " + id);
    }

}
