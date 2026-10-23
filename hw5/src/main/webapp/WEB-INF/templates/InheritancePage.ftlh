<#-- @ftlvariable name="message" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <p>${message}</p>

    <ul>
        <li><a href="/inheritance">action</a> (declared in page)</li>
        <li><a href="/inheritance?action=baseAction">baseAction</a> (declared in superclass)</li>
        <li><a href="/inheritance?action=baseActionFromInterface">baseActionFromInterface</a> (default method in
            interface)
        </li>
    </ul>
</@c.page>
