from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from stock_portfolios.models import Symbol, Portfolio
from django.core import serializers

def portfolios(request):
    results = Symbol.objects.order_by('-IPO_date');
    jsonResult = serializers.serialize('json', results);
    return HttpResponse(jsonResult, content_type="application/json")
    
def portfolio(request, symbolId):
    symbol = get_object_or_404(Symbol, pk=symbolId)
    jsonResult = serializers.serialize('json', [symbol]);
    just_object_result = jsonResult[1:-1]
    return HttpResponse(just_object_result, content_type="application/json")