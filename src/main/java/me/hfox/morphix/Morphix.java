package me.hfox.morphix;

import me.hfox.morphix.annotation.entity.Cache;
import me.hfox.morphix.cache.EntityCache;
import me.hfox.morphix.cache.EntityCacheImpl;
import me.hfox.morphix.exception.MorphixException;
import me.hfox.morphix.helper.entity.DefaultEntityHelper;
import me.hfox.morphix.helper.entity.EntityHelper;
import me.hfox.morphix.helper.name.NameHelper;
import me.hfox.morphix.helper.polymorphism.DefaultPolymorhpismHelper;
import me.hfox.morphix.helper.polymorphism.PolymorhpismHelper;
import me.hfox.morphix.mapping.ObjectMapper;
import me.hfox.morphix.mapping.ObjectMapperImpl;
import me.hfox.morphix.util.AnnotationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Morphix {

    private MorphixOptions options;
    private ObjectMapper mapper;
    private Map<String, EntityCache> caches;

    private EntityHelper entityHelper;
    private Map<Class<? extends NameHelper>, NameHelper> nameHelpers;
    private PolymorhpismHelper polymorhpismHelper;

    public Morphix() {
        this(null, null);
    }

    public Morphix(MorphixOptions options) {
        this(options, null);
    }

    public Morphix(ObjectMapper mapper) {
        this(null, mapper);
    }

    public Morphix(MorphixOptions options, ObjectMapper mapper) {
        this.options = (options == null ? MorphixOptions.builder().build() : options);
        this.mapper = (mapper == null ? new ObjectMapperImpl() : mapper);
        this.caches = new HashMap<>();

        this.entityHelper = new DefaultEntityHelper(this);
        this.nameHelpers = new HashMap<>();
        getNameHelper(MorphixDefaults.DEFAULT_NAME_HELPER);

        this.polymorhpismHelper = new DefaultPolymorhpismHelper();
    }

    public MorphixOptions getOptions() {
        return options;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public Map<String, EntityCache> getCaches() {
        return caches;
    }

    public EntityCache getCache() {
        return getCache(MorphixDefaults.DEFAULT_CACHE_NAME);
    }

    public EntityCache getCache(Class<?> cls) {
        Cache cache = AnnotationUtils.getHierarchicalAnnotation(cls, Cache.class);
        if (cache == null) {
            return getCache();
        }

        if (!cache.enabled()) {
            return null;
        }

        return getCache(cache.value());
    }

    public EntityCache getCache(String name) {
        EntityCache cache = caches.get(name);
        if (cache == null) {
            cache = new EntityCacheImpl(this);
            caches.put(name, cache);
        }

        return cache;
    }

    public boolean hasCache(String name) {
        return caches.get(name) != null;
    }

    public void setCache(String name, EntityCache cache) {
        caches.put(name, cache);
    }

    public EntityHelper getEntityHelper() {
        return entityHelper;
    }

    public void setEntityHelper(EntityHelper entityHelper) {
        this.entityHelper = entityHelper;
    }

    public Map<Class<? extends NameHelper>, NameHelper> getNameHelpers() {
        return nameHelpers;
    }

    public NameHelper getNameHelper(Class<? extends NameHelper> clazz) {
        NameHelper helper = nameHelpers.get(clazz);
        if (helper == null) {
            helper = createNameHelper(clazz);
            nameHelpers.put(clazz, helper);
        }

        return helper;
    }

    public NameHelper createNameHelper(Class<? extends NameHelper> clazz) {
        try {
            Constructor<? extends NameHelper> constructor = clazz.getConstructor(Morphix.class);
            return constructor.newInstance(this);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new MorphixException(ex);
        }
    }

    public PolymorhpismHelper getPolymorhpismHelper() {
        return polymorhpismHelper;
    }

    public void setPolymorhpismHelper(PolymorhpismHelper polymorhpismHelper) {
        this.polymorhpismHelper = polymorhpismHelper;
    }

}
