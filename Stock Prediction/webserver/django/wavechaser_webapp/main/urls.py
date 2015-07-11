from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'wavechaser_webapp.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^polls/', include('polls.urls', namespace="polls")),
    url(r'^stock_portfolios/', include('stock_portfolios.urls', namespace="stock_portfolios")),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^snippets', include('snippets.urls', namespace='snippets')),
    url(r'^ng_playground/', include('ng_playground.urls', namespace='ng_playground')),
)
