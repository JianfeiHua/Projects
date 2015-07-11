from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse
import os

def index(request):
    return render(request, 'ng_playground/index.html', {})
    
def template(request, view_name):
    # NOTE: Should return the raw html here!!!
    cur_dir = os.path.dirname(__file__)

    filename = cur_dir + '/templates/ng_playground/' + view_name + '.html';
    response = HttpResponse()
    for line in open(filename):
        response.write(line)

    return response