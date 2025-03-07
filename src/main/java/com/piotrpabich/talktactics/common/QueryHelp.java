package com.piotrpabich.talktactics.common;

import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;

@Log4j2
@SuppressWarnings({"unchecked","all"})
public class QueryHelp {

    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<>();
        if(query == null){
            return cb.and(list.toArray(new Predicate[0]));
        }
        try {
            Map<String, Join> joinKey = new HashMap<>();
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);
                    if (ObjectUtils.isEmpty(val)) {
                        continue;
                    }
                    Join join = null;
                    if (!ObjectUtils.isEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(cb.lower(root.get(s).as(String.class)), "%" + val.toString().toLowerCase() + "%"));
                        }
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(cb.or(orPredicate.toArray(p)));
                        continue;
                    }
                    if (!ObjectUtils.isEmpty(joinName)) {
                        join = joinKey.get(joinName);
                        if(join == null){
                            String[] joinNames = joinName.split(">");
                            for (String name : joinNames) {
                                switch (q.join()) {
                                    case LEFT:
                                        if(!ObjectUtils.isEmpty(join) && !ObjectUtils.isEmpty(val)) {
                                            join = join.join(name, JoinType.LEFT);
                                        } else {
                                            join = root.join(name, JoinType.LEFT);
                                        }
                                        break;
                                    case RIGHT:
                                        if(!ObjectUtils.isEmpty(join) && !ObjectUtils.isEmpty(val)) {
                                            join = join.join(name, JoinType.RIGHT);
                                        } else {
                                            join = root.join(name, JoinType.RIGHT);
                                        }
                                        break;
                                    case INNER:
                                        if(!ObjectUtils.isEmpty(join) && !ObjectUtils.isEmpty(val)) {
                                            join = join.join(name, JoinType.INNER);
                                        } else {
                                            join = root.join(name, JoinType.INNER);
                                        }
                                        break;
                                    default: break;
                                }
                            }
                            joinKey.put(joinName, join);
                        }
                    }
                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType),val));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThanOrEqualTo(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(cb.lessThanOrEqualTo(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_NQ:
                            list.add(cb.lessThan(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), val.toString() + "%"));
                            break;
                        case IN:
                            if (!CollectionUtils.isEmpty((Collection<Object>)val)) {
                                list.add(getExpression(attributeName,join,root).in((Collection<Object>) val));
                            }
                            break;
                        case NOT_IN:
                            if (!CollectionUtils.isEmpty((Collection<Object>)val)) {
                                list.add(getExpression(attributeName,join,root).in((Collection<Object>) val).not());
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(cb.notEqual(getExpression(attributeName,join,root), val));
                            break;
                        case NOT_NULL:
                            list.add(cb.isNotNull(getExpression(attributeName,join,root)));
                            break;
                        case IS_NULL:
                            list.add(cb.isNull(getExpression(attributeName,join,root)));
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>)val);
                            if(between.size() == 2){
                                list.add(cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                        (Comparable) between.get(0), (Comparable) between.get(1)));
                            }
                            break;
                        case FIND_IN_SET:
                            list.add(cb.greaterThan(cb.function("FIND_IN_SET", Integer.class,
                                    cb.literal(val.toString()), root.get(attributeName)), 0));
                            break;
                        default: break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int size = list.size();
        return cb.and(list.toArray(new Predicate[size]));
    }

    @SuppressWarnings("unchecked")
    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (!ObjectUtils.isEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
