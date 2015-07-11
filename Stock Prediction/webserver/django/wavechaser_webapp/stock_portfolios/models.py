from django.db import models

class Symbol(models.Model):
    symbol = models.CharField(max_length=200)
    full_name = models.CharField(max_length=200)
    IPO_date = models.DateTimeField()
    description = models.CharField(max_length=1000)

class Portfolio(models.Model):
    symbol = models.ForeignKey(Symbol)
    date = models.DateTimeField()
    min_price = models.FloatField()
    max_price = models.FloatField()
    open_price = models.FloatField()
    close_price = models.FloatField()
    dividend = models.FloatField()
    trade_volume = models.IntegerField()
