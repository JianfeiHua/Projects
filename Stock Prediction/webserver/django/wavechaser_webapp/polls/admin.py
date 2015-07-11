from django.contrib import admin
from polls.models import Choice, Question

# Register your models here.
class ChoiceInline(admin.StackedInline):
    model = Choice
    extra = 1

class PollAdmin(admin.ModelAdmin):
    list_display = ('question_text', 'pub_date', 'was_published_recently')
    fieldsets = [
        (None,               {'fields': ['question_text']}),
        ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
    ]
    inlines = [ChoiceInline]
    list_filter = ['pub_date']
    search_fields = ['question']

admin.site.register(Question, PollAdmin)
