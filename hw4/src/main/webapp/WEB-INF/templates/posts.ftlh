<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if user_id?? && user_id?is_number>
        <@c.postlist c.postsBy(user_id)/>
    </#if>
</@c.page>
