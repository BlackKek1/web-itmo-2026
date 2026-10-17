<#assign POST_PREVIEW_LENGTH = 350/>
<#assign SIDEBAR_POSTS_COUNT = 3/>

<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#-- @ftlvariable name="uri" type="java.lang.String" -->
<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user=user nameOnly=true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/users", "name": "Users"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <a href="${item.href}" <#if item.href == uri> class="selected-page"</#if>>${item.name}</a>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <#if p_index == SIDEBAR_POSTS_COUNT>
                <#break>
            </#if>
            <section>
                <div class="header">Post #${p.id}</div>
                <div class="body">${shorten(p.text)}</div>
                <div class="footer">
                    <a href="/post?post_id=${p.id}">View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly=false>
    <#if nameOnly>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    <#else>
        <a href="/user?handle=${user.handle}" class="user user-${user.color?lower_case}">${user.handle}</a>
    </#if>
</#macro>

<#macro post item full=false>
    <#local author = findBy(users, "id", item.userId)/>
    <article>
        <div class="title">${item.title}</div>
        <div class="information">By <@userlink author/></div>
        <div class="body">${full?then(item.text, shorten(item.text))}</div>
    </article>
</#macro>

<#function shorten text>
    <#if text?length gt POST_PREVIEW_LENGTH>
        <#return text?substring(0, POST_PREVIEW_LENGTH) + "..."/>
    </#if>
    <#return text/>
</#function>

<#macro postlist items>
    <#list items?reverse as item>
        <@post item/>
    </#list>
</#macro>

<#function postsBy userId>
    <#return posts?filter(p -> p.userId == userId)/>
</#function>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function indexBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item_index/>
        </#if>
    </#list>
    <#return -1/>
</#function>
