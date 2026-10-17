<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign index = -1/>
    <#if handle??>
        <#assign index = c.indexBy(users, "handle", handle)/>
    <#elseif user_id?? && user_id?is_number>
        <#assign index = c.indexBy(users, "id", user_id)/>
    </#if>

    <#if index gte 0>
        <#assign u = users[index]/>
        <div class="profile">
            <img src="/img/no-title.jpg" alt="${u.name}">
            <h2><@c.userlink u/></h2>
            <ul>
                <li>id: ${u.id}</li>
                <li>handle: ${u.handle}</li>
                <li>name: ${u.name}</li>
                <li>posts: ${c.postsBy(u.id)?size}</li>
                <li><a href="/posts?user_id=${u.id}">Posts</a></li>
            </ul>
        </div>
        <div class="arrows">
            <#if index gt 0>
                <a href="/user?handle=${users[index - 1].handle}">&larr;</a>
            <#else>
                <span class="disabled">&larr;</span>
            </#if>
            <#if index lt users?size - 1>
                <a href="/user?handle=${users[index + 1].handle}">&rarr;</a>
            <#else>
                <span class="disabled">&rarr;</span>
            </#if>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>
