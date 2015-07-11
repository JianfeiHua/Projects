from django.contrib import admin
from stock_portfolios.models import Symbol, Portfolio

class PortfolioInline(admin.StackedInline):
    model = Portfolio
    extra = 1

class SymbolAdmin(admin.ModelAdmin):
    list_display = ('symbol', 'full_name', 'IPO_date', 'description')
    '''
    fieldsets = [
        (None,               {'fields': ['symbol']}),
        ('Date information', {'fields': ['full_name'], 'classes': ['collapse']}),
    ]
    '''
    inlines = [PortfolioInline]
    list_filter = ['IPO_date']
    search_fields = ['symbol']

admin.site.register(Symbol, SymbolAdmin)
