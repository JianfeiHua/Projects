from django.conf.urls import patterns, url

from stock_portfolios import views

urlpatterns = patterns('',
    url(r'^$', views.IndexView.as_view(), name='index'),
    url(r'^(?P<symbol_id>\w+)/$', views.portfolio, name='portfolio'),
)
