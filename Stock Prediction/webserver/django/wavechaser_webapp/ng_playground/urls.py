from django.conf.urls import patterns, url

from ng_playground import views, api

urlpatterns = patterns('',
    url(r'^$', views.index, name='index'),
    url(r'^portfolios', views.index, name='index'),
)

urlpatterns += patterns('',
    url(r'^templates/(?P<view_name>\w+).html$', views.template, name='template'),
    url(r'^api/portfolios/$', api.portfolios, name='apiPortfolios'),
    url(r'^api/portfolios/(?P<symbolId>\d+)$', api.portfolio, name='apiPortfolio'),
)
