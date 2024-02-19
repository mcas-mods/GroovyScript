package com.cleanroommc.groovyscript.api.documentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Documentation information for generic methods of a registry.<br>
 * All fields are optional, and most have default values generated in {@link Example}.
 *
 * <ul>
 *     <li>{@link #description()} is a localization key that is autogenerated to be
 *     <code>
 *         groovyscript.wiki.{@link com.cleanroommc.groovyscript.compat.mods.GroovyContainer#getModId() GroovyContainer#getModId()}.{@link com.cleanroommc.groovyscript.registry.VirtualizedRegistry#getName() VirtualizedRegistry#getName()}.{@link Method#getName()}
 *     </code>
 *     </li>
 *     <li>{@link #example()} is an array of {@link Example}s In situations where either a single {@link Example} with multiple lines or
 *     multiple {@link Example}s could be used, using multiple {@link Example}s is preferable.</li>
 *     <li>{@link #type()} is a {@link Type} determining what type of Method is being annotated. Different types are placed in different locations of the wiki page and examples file.</li>
 *     <li>{@link #priority()} is an integer that influences the sorting of the {@link MethodDescription} relative to other {@link MethodDescription}s of the same {@link Type}.</li>
 * </ul>
 *
 * @see Type
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodDescription {

    /**
     * The localization key for a description of the compat, will default to generating
     * <code>
     * groovyscript.wiki.{@link com.cleanroommc.groovyscript.compat.mods.GroovyContainer#getModId() GroovyContainer#getModId()}.{@link com.cleanroommc.groovyscript.registry.VirtualizedRegistry#getName() VirtualizedRegistry#getName()}.{@link Method#getName()}
     * </code>
     *
     * @return localization key for method description
     */
    String description() default "";

    /**
     * An array of examples, which will then be formatted and generated for both the wiki and the text script files.
     *
     * @return array of examples
     * @see Example
     */
    Example[] example() default {};

    /**
     * Determines if the method is used to add an entry to the registry, remove an entry from the registry, or simply get information about the registry.
     * Defaults to {@code Type.REMOVAL}
     *
     * @return if the method adds entries, removes entries, or is purely a query of information. Defaults to removes entries ({@link Type#REMOVAL})
     * @see Type
     */
    Type type() default Type.REMOVAL;

    /**
     * Priority of the method, relative to other methods in the same class and with the same value of remove.
     * Priorities sort entries such that lowest is first, with ties being broken via alphabetical sorting of the method name.
     *
     * @return the method priority (relative to other methods of the same type)
     */
    int priority() default 1000;

    /**
     * Determines the type of method, which changes where the method is described for the wiki and
     * where the example code for the method is placed in the examples files.
     */
    enum Type {

        /**
         * Instructs that the method neither adds nor removes entries from the registry, and only checks information about the registry.
         * Often used in removing entries matching complex requirements that are not otherwise supported.
         */
        QUERY,

        /**
         * Instructs that the method adds entries to the registry, indicating it should be placed in the ADDITION category on the wiki and
         * below {@linkplain #REMOVAL}-type methods in examples files.
         */
        ADDITION,

        /**
         * Instructs that the method removes entries to the registry, indicating it should be placed in the REMOVAL category on the wiki and
         * above {@linkplain #ADDITION}-type methods in examples files.
         */
        REMOVAL,
        /**
         * Modifies or changes a value or setting. Does not add, remove, or query the registry.
         */
        VALUE
    }

}
