package cn.bfreeman.common.util;

import cn.bfreeman.common.databind.DateDeserializer;
import cn.bfreeman.common.enums.EnumTrait;
import cn.bfreeman.common.enums.EnumTraitParser;
import cn.bfreeman.common.exception.FatalException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.TimeZone;

/**
 * JSON工具类
 *
 * @author dang.you
 */
public final class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 接受JSON中存在注释
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 接受JSON中不使用双引号包围Key
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 接受JSON中用单引号代替双引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 接受JSON中存在控制字符在非字符串中的情况
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 反序列化中忽略未知JSON字段
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 不序列化Map中值为null的数据
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        // 序列化空属性的bean不报错
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        SimpleModule m = new SimpleModule("mice-common", new Version(1, 0, 0, null, "me.huakai.mice", "mice-common"));


        /*
         *
         * 设置时区, 避免序列化后少一天的问题
         * 从数据库查到日期后, 默认是CST时区的
         * 如果不设置, jackson 会默认把日期转化成 GMT时区对应的日期, 然后序列化成对应的字符串
         * 这样序列化后的日期字符串丢失了时区信息, 得到的日期字符串也不是我们想要的日期字符串了
         * 如果自定义了日期反序列化方式, 反序列化的时候会取系统默认的时区,也就是GMT时区,这时候日期就和序列化时不一致了
         *
         * 日期的序列化和反序列化时需要注意时区的设置
         */
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        m.addDeserializer(Date.class, new DateDeserializer());

        m.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<Enum> modifyEnumSerializer(SerializationConfig config
                    , JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                return new JsonSerializer<Enum>() {
                    @Override
                    public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                        if (value instanceof EnumTrait) {
                            gen.writeNumber(((EnumTrait) value).getCode());
                        } else {
                            gen.writeString(value.name());
                        }
                    }
                };
            }
        });
        m.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<Enum> modifyEnumDeserializer(DeserializationConfig config
                    , final JavaType type, BeanDescription beanDesc, final JsonDeserializer<?> deserializer) {
                return new JsonDeserializer<Enum>() {
                    @Override
                    public Enum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        if (EnumTrait.class.isAssignableFrom(type.getRawClass())) {
                            return (Enum) EnumTraitParser.codeOf((Class<? extends EnumTrait>) type.getRawClass(), jp.getIntValue());
                        } else {
                            return Enum.valueOf((Class<? extends Enum>) type.getRawClass(), jp.getValueAsString());
                        }
                    }
                };
            }
        });

        OBJECT_MAPPER.registerModule(m);
    }

    private JsonUtil() {
        throw new UnsupportedOperationException();
    }

    public static String encode(Object obj) throws IOException {
        if (obj == null) {
            //防止为null时返回"null"
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }


    private static final String JSON_EMPTY = "{}";

    /**
     * @param obj
     * @return
     */
    public static String encodeQuietly(Object obj) {
        if (obj == null) {
            return JSON_EMPTY;
        }
        try {
            return encode(obj);
        } catch (IOException e) {
            logger.error("序列化失败, obj:{}, reason:{}", obj, e.getMessage(), e);
            throw new FatalException(e.getMessage(), e);
        }
    }

    public static <T> T decodeQuietly(String json, Class<T> clazz) {
        try {
            return decode(json, clazz);
        } catch (IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }

  public static <T> T decodeQuietly(String json, TypeReference<T> typeReference) {
        try {
            return decode(json, typeReference);
        } catch (IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }


    public static <T> T decode(String json, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static JsonNode decode(String json) throws IOException {
        return OBJECT_MAPPER.readTree(json);
    }

    public static <T> T decode(JsonNode jsonNode, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(OBJECT_MAPPER.treeAsTokens(jsonNode), clazz);
    }

    public static <T> T decode(String json, TypeReference typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(json, typeReference);
    }

    public static <T> T decode(String json, ParameterizedType parameterizedType) throws IOException {
        return (T) OBJECT_MAPPER.readValue(json, convertToJavaType(parameterizedType));
    }

    public static <T> T decode(String json, ParameterizedTypeReference<T> typeReference) throws IOException {
        return (T) OBJECT_MAPPER.readValue(json, convertToJavaType(typeReference));
    }

    public static <T> T decode(JsonNode jsonNode, ParameterizedTypeReference<T> typeReference) throws IOException {
        return (T) OBJECT_MAPPER.readValue(OBJECT_MAPPER.treeAsTokens(jsonNode), convertToJavaType(typeReference));
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 根据path解析json串里面的某一个值，只支持简单的name.name1.name2方式
     *
     * @param json      完整JSON串
     * @param path      类似xPath的属性路径
     * @param valueType 属性路径对应的JSON节点需要反序列化的Java类型
     * @return 若path对应的节点不存在，则返回null，否则返回反序列化之后的对象
     */
    public static <T> T decode(String json, String path, Class<T> valueType) throws IOException {
        JsonNode node = OBJECT_MAPPER.readTree(json);
        Iterable<String> names = Splitter.on('.').split(path);
        for (String name : names) {
            node = node.path(name);
        }

        if (node.isMissingNode()) {
            return null;
        } else {
            return (T) OBJECT_MAPPER.reader(valueType).readValue(node);
        }
    }

    private static <T> JavaType convertToJavaType(ParameterizedTypeReference<T> typeReference) {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        return typeFactory.constructType(typeReference.getType());
    }

    private static JavaType convertToJavaType(ParameterizedType parameterizedType) {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        return typeFactory.constructType(parameterizedType);
    }
}
