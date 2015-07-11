from django.shortcuts import render, get_object_or_404
from django.http import HttpResponseRedirect, HttpResponse
from django.core.urlresolvers import reverse
from django.views import generic
from stock_portfolios.models import Symbol, Portfolio

class IndexView(generic.ListView):
    template_name = 'stock_portfolios/index.html'
    context_object_name = 'symbol_list'

    def get_queryset(self):
        return Symbol.objects.order_by('-IPO_date')

def portfolio(request, symbol_id):
    symbol = get_object_or_404(Symbol, pk=symbol_id)
    return render(request, 'stock_portfolios/portfolio.html', {'symbol': symbol})
